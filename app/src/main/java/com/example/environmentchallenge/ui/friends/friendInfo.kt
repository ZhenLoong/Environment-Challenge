package com.example.environmentchallenge.ui.friends

class friendInfo {
    private var name:String = ""
    private var id:String = ""
    private var selected:Boolean = false

    fun getName():String{
        return name
    }

    fun setName(name:String){
        this.name = name
    }

    fun getID():String{
        return id
    }

    fun setID(id:String){
        this.id = id
    }

    fun isSelected():Boolean{
        return selected
    }

    fun setSelected(selected:Boolean){
        this.selected = selected
    }

    override fun toString():String{
        return name
    }
}