package com.example.appdev.ui.friends

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R
import com.example.appdev.database.entities.FriendEntity
import com.example.appdev.database.entities.FriendRequestEntity

class FriendsFragment : Fragment() {

    private val friendViewModel: FriendsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)
        val linearLayoutContainer: LinearLayout = view.findViewById(R.id.container)
        val friendRequestContainer: LinearLayout = view.findViewById(R.id.friendRequestContainer)
        val addFriendEditText: EditText = view.findViewById(R.id.addFriendEditText)
        val addFriendButton: Button = view.findViewById(R.id.addFriendButton)
        val backToAccountButton: Button = view.findViewById(R.id.backToAccountButton)

        friendViewModel.friends.observe(viewLifecycleOwner, Observer { friends ->
            Log.d("FriendsFragment", "Friends observed: $friends")
            linearLayoutContainer.removeAllViews()
            for (friend in friends) {
                val friendView = addFriendView(inflater, linearLayoutContainer, friend)
                linearLayoutContainer.addView(friendView)
            }
        })

        friendViewModel.friendRequests.observe(viewLifecycleOwner, Observer { requests ->
            Log.d("FriendsFragment", "Friend requests observed: $requests")
            friendRequestContainer.removeAllViews()
            for (request in requests) {
                val requestView = addFriendRequestView(inflater, friendRequestContainer, request)
                friendRequestContainer.addView(requestView)
            }
        })

        addFriendButton.setOnClickListener {
            val email = addFriendEditText.text.toString()
            if (email.isNotEmpty()) {
                Log.d("FriendsFragment", "Adding friend with email: $email")
                friendViewModel.sendFriendRequest(requireContext(), email)
                addFriendEditText.text.clear()
            }
        }

        backToAccountButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun addFriendView(inflater: LayoutInflater, container: ViewGroup?, friend: FriendEntity): View {
        val view = inflater.inflate(R.layout.item_friend, container, false)
        val friendName: TextView = view.findViewById(R.id.friend_name)
        val deleteFriendButton: ImageButton = view.findViewById(R.id.remove_friend_button)

        friendName.text = friend.name

        deleteFriendButton.setOnClickListener {
            Log.d("FriendsFragment", "Removing friend: ${friend.name}")
            showConfirmRemoveFriendDialog {
                friendViewModel.removeFriend(requireContext(), friend)
            }
        }

        return view
    }

    private fun addFriendRequestView(inflater: LayoutInflater, container: ViewGroup?, request: FriendRequestEntity): View {
        val view = inflater.inflate(R.layout.item_friend_request, container, false)
        val acceptButton: Button = view.findViewById(R.id.acceptButton)
        val declineButton: Button = view.findViewById(R.id.declineButton)

        acceptButton.setOnClickListener {
            Log.d("FriendsFragment", "Accepting friend request: $request")
            friendViewModel.acceptFriendRequest(requireContext(), request)
        }

        declineButton.setOnClickListener {
            Log.d("FriendsFragment", "Declining friend request: $request")
            friendViewModel.declineFriendRequest(requireContext(), request)
        }

        return view
    }

    private fun showConfirmRemoveFriendDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to remove this friend?")
            .setPositiveButton("Yes") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
