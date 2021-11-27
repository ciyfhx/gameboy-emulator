package com.ciyfhx.emu.viewer

import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.scene.control.ListView
import javafx.scene.control.ScrollBar

fun interface LazyLoader{
    fun loadItems(index: Int, size: Int): Collection<String>
}

class LazyListView(
    val loader: LazyLoader,
    val totalItems: Int
) : ListView<String>() {

    companion object {
        private const val STEP_SIZE = 10
    }

    private val listItems = FXCollections.observableArrayList<String>()
    private lateinit var scrollBar: ScrollBar

    private var start = 0
    private var step = STEP_SIZE


    fun init() {
        this.items = listItems
        scrollBar = getListViewScrollBar()
        scrollBar.valueProperty().addListener { observable, oldValue, newValue ->
            val pos = newValue.toDouble()
            if(pos == scrollBar.max && step <= totalItems) {
                loadItemsAndPopulateInListItem(start)
            }
        }
        loadInitialData()
    }

    private fun loadInitialData(){
        this.loadItemsAndPopulateInListItem(0)
    }

    private fun loadItemsAndPopulateInListItem(index: Int){
        listItems.addAll(index, loader.loadItems(index, STEP_SIZE))
        start = step
        step += STEP_SIZE
    }

    private fun getListViewScrollBar(): ScrollBar {
        for(node in this.lookupAll(".scroll-bar")){
            if(node is ScrollBar && node.orientation == Orientation.VERTICAL) {
                return node
            }
        }
        throw IllegalStateException("Unable to find scroll bar in list view")
    }


}