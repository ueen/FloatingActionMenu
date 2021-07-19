package de.ueen.fabmenusample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.ueen.fabmenu.FloatingActionMenu
import de.ueen.fabmenusample.databinding.ActivityMainBinding
import slush.Slush


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        Slush.SingleType<String>()
            .setItemLayout(android.R.layout.simple_list_item_1)
            .setLayoutManager(
                LinearLayoutManager(
                    this,
                    RecyclerView.VERTICAL,
                    false
                )
            )
            .setItems(List(50) {"test"})
            .onItemClick { view: View, i: Int ->  }
            .onBind { view, s -> view.findViewById<TextView>(android.R.id.text1).text = s }
            .into(bind.recyclerView)

        bind.fab.addAction(FloatingActionMenu.ActionItem(R.drawable.ic_baseline_filter_list_24,"first"))
        bind.fab.addAction(FloatingActionMenu.ActionItem(R.drawable.ic_baseline_edit_24,"second"))
        bind.fab.addAction(FloatingActionMenu.ActionItem(R.drawable.ic_baseline_done_24,"third"))

        bind.fab.selectActionItemToFab("first")

        bind.fab.setOnActionClickListener(true) { actionItem ->
            Log.d("TAG", "onCreate: "+actionItem.tag)
        }
    }
}