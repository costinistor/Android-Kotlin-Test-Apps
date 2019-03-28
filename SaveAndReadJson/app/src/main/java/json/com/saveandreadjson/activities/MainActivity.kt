package json.com.saveandreadjson.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import json.com.saveandreadjson.R
import json.com.saveandreadjson.constants.PERSON_FILE
import json.com.saveandreadjson.data.loadDataFromFile
import json.com.saveandreadjson.data.savingDataToFile
import json.com.saveandreadjson.models.Person
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCreate.setOnClickListener { createJSON() }

        btnDisplay.setOnClickListener { readJSONObject() }
    }

    fun createJSON(){
        val type = etType.text.toString()
        val name = etName.text.toString()


        val person1 = Person(1, "Boss", "Vasile Bostan")
        val person2 = Person(2, "Sclav", "Maeve Beton")

//        val person = arrayListOf(person1, person2)
//        val per = Persons(person)

        val person: HashMap<String, String> = hashMapOf()
        person["loc"] = "Pascani"
        person["time"] = "Secolul 20"
        person["tara"] = "Klingon"
        //val per = Persons(person)
        val hasPer = Pers(person)

        val gson = Gson()
        val file = gson.toJson(hasPer)
        savingDataToFile(this, PERSON_FILE, file)
    }

    fun readJSON(){
        val response = loadDataFromFile(this, PERSON_FILE, "none")
        tvResult.text = response
        val gson = Gson()

        val dataType: Type = object : TypeToken<List<Person>>(){}.type

        val person: List<Person> = gson.fromJson(response, dataType)

        for (i in 0 until person.count()){
            println(person[i].name)
        }
    }

    fun readJSONObject(){
        val response = loadDataFromFile(this, PERSON_FILE, "none")
        //tvResult.text = response
        val gson = Gson()

        val person = gson.fromJson(response, Pers::class.java)

        println(person.persons["loc"])

        var st = "${person.persons["time"]} \n"
        st += "${person.persons["lo"]} \n"
        st += "${person.persons["tara"]} \n"

        tvResult.text = st

    }

    class Persons(val persons:List<Person>)
    class Pers(val persons:HashMap<String, String>)
}
