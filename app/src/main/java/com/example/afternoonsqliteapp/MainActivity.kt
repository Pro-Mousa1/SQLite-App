package com.example.afternoonsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var editname:EditText
    lateinit var editEmail:EditText
    lateinit var editIdNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editname = findViewById(R.id.mEditName)
        editEmail = findViewById(R.id.mEditEmail)
        editIdNumber = findViewById(R.id.mEditNumber)
        btnSave = findViewById(R.id.mBtnSave)
        btnView = findViewById(R.id.mBtnView)
        btnDelete = findViewById(R.id.mBtnDelete)
        // Create a database called eMobilisDB
        db = openOrCreateDatabase("eMobilisDB", Context.MODE_PRIVATE, null)
        // Create a table inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        // sET OnClick listeners to the buttons
        btnSave.setOnClickListener {
            var name = editname.text.toString().trim()
            var email = editEmail.text.toString().trim()
            var idnumber = editIdNumber.text.toString().trim()
            // Check if the user is submitting empty fields
            if (name.isEmpty() || email.isEmpty() || idnumber.isEmpty()){
                message("EMPTY FIELDS", "Please fill all inputs")
            }else{
                // Proceed to save data
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+idnumber+"')")
                clear()
                message("SUCCESS!!", "User saved successfully!!!")
            }
        }
        btnView.setOnClickListener {
            var cursor = db.rawQuery("SELECT * FROM users", null)
            // Check if there's any record in the database
            if (cursor.count == 0){
                message("NO RECORD!!!", "Sorry, no users were found!!!")
            }else{
                // Use a string buffer to append all users retrieved using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n")
                }
                message("USERS", buffer.toString())
            }
        }
        btnDelete.setOnClickListener {
            var idNumber = editIdNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELD", "Please fill the ID field")
            }else{
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNumber+"'", null)
                if (cursor.count == 0){
                    message("NO RECORD!", "Sorry, no user found!!")
                }else{
                    db.execSQL("DELETE * FROM users WHERE kitambulisho='"+idNumber+"'")
                    clear()
                    message("SUCCESS!!", "User deleted successfully!!")
                }
            }
        }

    }
    fun message(title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Cancel", null)
        alertDialog.create().show()
    }
    fun clear(){
        editname.setText("")
        editEmail.setText("")
        editIdNumber.setText("")
    }
}