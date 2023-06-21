package com.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.home.databinding.ItemMemeBinding
import com.model.Item

class MemeAdapter(
    private var memes: MutableList<String>,
    private var currentItem: (String) -> Unit
) :
    RecyclerView.Adapter<MemeAdapter.MemeViewHolder>() {
    private lateinit var binding: ItemMemeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        binding = ItemMemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemeViewHolder(binding, parent.context)
    }

    class MemeViewHolder(private val binding: ItemMemeBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String?) {
            if (item?.contains(".mp4") == false) {
                binding.memeImg.visibility = View.VISIBLE
                binding.memeVideo.visibility = View.GONE
                binding.playerView.visibility = View.GONE
                Glide.with(context).load(item.orEmpty()).error(android.R.drawable.stat_notify_error)
                    .into(binding.memeImg)
            } else {
                binding.memeImg.visibility = View.GONE
                binding.memeVideo.visibility = View.VISIBLE
                binding.playerView.visibility = View.VISIBLE

                val player = ExoPlayer.Builder(context).build()
// Bind the player to the view.
                binding.memeVideo.player = player
                binding.playerView.player = player
                // Build the media item.

                var mediaItem = MediaItem.fromUri(item.orEmpty())

                // Set the media metadata
                var mediaMetadata = MediaMetadata.Builder().setTitle("Video Title").build()

// Set the media item to be played.
                player.setMediaItem(mediaItem)
// Prepare the player.
                player.prepare()
                binding.memeVideo.showTimeoutMs = 0
// Start the playback.
                binding.playerView.setOnFocusChangeListener { v, hasFocus ->
                    if(hasFocus)
                        player.play()
                    else
                        player.pause()
                }
              //  player.play()
                //    Glide.with(context).load(android.R.drawable.stat_notify_error).into(binding.memeImg)


            }/*    binding.memeImg.setOnClickListener(object : DoubleClickListener() {
                 override fun onDoubleClick(v: View?) {
                     binding.like.setImageResource(chooseLikeIcon())
                 }
             })

             binding.like.setOnClickListener {
                 binding.like.setImageResource(chooseLikeIcon())
             }
             binding.share.setOnClickListener {
                 binding.root.context.apply {
                     val intent = Intent(Intent.ACTION_SEND)
                     intent.putExtra(
                         Intent.EXTRA_TEXT,
                         this.getString(R.string.share_descrition, googleResponse?.url.orEmpty())
                     )
                     intent.type = "text/plain"
                     this.startActivity(
                         intent
                     )
                 }
             }*/
        }

        /* fun chooseLikeIcon(): Int {
             binding.like.tag = if (binding.like.tag ==
                 ic_like
             )
                 ic_unlike
             else
                 ic_like
             return binding.like.tag as Int
         }*/
    }


    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        holder.bind(memes[position])
        currentItem(memes[position])
    }

    override fun getItemCount(): Int = memes.size ?: 0

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun submitList(items: MutableList<String>) {
        items.forEach { memes.add(it) }
        notifyDataSetChanged()
    }
}

abstract class DoubleClickListener : View.OnClickListener {
    private var lastClick: Long = 0
    override fun onClick(v: View?) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClick < DOUBLE_CLICK_TIME_DELTA)
            onDoubleClick(v)
        lastClick = clickTime
    }

    abstract fun onDoubleClick(v: View?)

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA = 300
    }
}