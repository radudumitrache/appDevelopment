package com.example.appdev.ui.friends

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R

class FriendsFragment : Fragment() {

    private val friendViewModel: ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)
        val linearLayoutContainer: LinearLayout = view.findViewById(R.id.container)

        friendViewModel.friends.observe(viewLifecycleOwner, Observer { friends ->
            linearLayoutContainer.removeAllViews()
            for (friend in friends) {
                val friendView = addFriend(inflater, linearLayoutContainer, friend)
                linearLayoutContainer.addView(friendView)
            }
        })

        return view
    }

    private fun addFriend(inflater: LayoutInflater, container: ViewGroup?, friend: ViewModel.Friend): View {
        val view = inflater.inflate(R.layout.item_friend, container, false)
        val picture: ImageView = view.findViewById(R.id.picture)
        val friendName: TextView = view.findViewById(R.id.friend_name)
        val compareButton: ImageButton = view.findViewById(R.id.compare_button)
        val deleteFriend: ImageButton = view.findViewById(R.id.remove_friend_button)

        friendName.text = friend.name

        deleteFriend.setOnClickListener {
            showConfirmRemoveFriendDialog {
                friendViewModel.removeFriend(friend)
            }
        }

        return view
    }

    private fun showConfirmRemoveFriendDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to remove your friend?")
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
