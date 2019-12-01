package com.example.oysterrecovery

data class Route (
    var routes: String = "") {

    lateinit var listOfRoutes : ArrayList<Int>


    fun addArrRoutes() {
        val arr = routes.split(',')

        for(s in arr) {
            listOfRoutes.add(s.toInt())
        }
    }

    fun getArrRoutes(): ArrayList<Int> {
        return listOfRoutes
    }
}

