package com.ciyfhx.emu.viewer

import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.scene.control.ListView
import javafx.scene.control.ScrollBar
import javafx.scene.input.ScrollEvent

fun interface LazyLoader{
    fun loadItems(index: Int, size: Int): Collection<String>
}

class LazyListView(
    val loader: LazyLoader,
    val totalItems: Int
) : ListView<String>() {

    companion object {
        private const val STEP_SIZE = 1000
    }

    private val listItems = FXCollections.observableArrayList<String>()
    private var scrollBar: ScrollBar? = null

    private var start = 0
    private var step = STEP_SIZE

    init {
        init()
    }

    fun init() {
        loadInitialData()
        this.items = listItems
        addEventFilter(ScrollEvent.ANY) {
            if(scrollBar == null){
                scrollBar = getListViewScrollBar()
                scrollBar!!.valueProperty().addListener { observable, oldValue, newValue ->
                    val pos = newValue.toDouble()
                    if(pos == scrollBar!!.max && step <= totalItems) {
                        loadItemsAndPopulateInListItem(start)
                    }
                }
            }
        }

    }

    private fun loadInitialData(){
        this.loadItemsAndPopulateInListItem(0)
    }

    private fun loadItemsAndPopulateInListItem(index: Int){
        start = step
        step = (step + STEP_SIZE).coerceIn(0 until totalItems)
        listItems.addAll(index, loader.loadItems(index, step))
    }

    private fun getListViewScrollBar(): ScrollBar {
        for(node in this.lookupAll(".scroll-bar")){
            if(node is ScrollBar && node.orientation == Orientation.VERTICAL) {
                return node
            }
        }
        throw IllegalStateException("Unable to find scroll bar in list view")
    }

    override fun refresh() {
        super.refresh()


        getListViewScrollBar()
    }
}