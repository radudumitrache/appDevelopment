package com.example.appdev.ui.friends

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        // Get the LinearLayout container
        val linearLayoutContainer: LinearLayout = view.findViewById(R.id.container)

        // Observe the ViewModel data
        friendViewModel.friends.observe(viewLifecycleOwner, Observer { friends ->
            // Clear the container before adding new views
            linearLayoutContainer.removeAllViews()

            // Dynamically add each friend view to the container
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

        // Optionally, you can set onClickListeners for buttons here

        return view
    }
}
