package com.example.oysterrecovery

class Restaurant {
    var address: String = ""
    var name: String = ""
    var oysterCapacity: Int = 0
    var oysterNumber: Int = 0


    constructor(address: String, name: String, oysterCapacity: Int, oysterNumber: Int) {
        this.address = address
        this.name = name
        this.oysterCapacity = oysterCapacity
        this.oysterNumber = oysterNumber
    }

    override fun toString(): String {
        return "Restaurant(address='$address', name='$name', oysterCapacity=$oysterCapacity, oysterNumber=$oysterNumber)"
    }


}