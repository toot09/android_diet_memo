package com.ujone.diet_memo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : AppCompatActivity() {

    val dataModelList = mutableListOf<DataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database
        // .child(Firebase.auth.currentUser!!.uid) : UID 별 처리!
        val myRef = database.getReference("myMemo").child(Firebase.auth.currentUser!!.uid)

        // 화면의 리스트 뷰 가져오기
        val listView = findViewById<ListView>(R.id.mainLV)
        // 어뎁터 가져오기
        val adapter_list = ListViewAdapter(dataModelList)

        listView.adapter = adapter_list

        // 데이터 가져오기
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // change 될때마다 중복으로 넣어준다. (아래 adapter_list.notifyDataSetChanged())
                dataModelList.clear()
                for(dataModel in snapshot.children) {
                    dataModelList.add(dataModel.getValue(DataModel::class.java)!!)
                }
                // 데이터 변경 될 때마다 어댑터 리프래시 해줘라
                adapter_list.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val writeButton = findViewById<ImageView>(R.id.writeBtn)
        writeButton.setOnClickListener {

            // 다이얼로그 처리
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custome_dialog, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("운동 메모 다이얼로그")

            val mAlertDialog = mBuilder.show()

            val dateSelectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)

            // mAlertDialog : 다이어로그
            dateSelectBtn?.setOnClickListener {

                val today = GregorianCalendar()
                val year : Int = today.get(Calendar.YEAR)
                val month : Int = today.get(Calendar.MONTH)
                val date : Int = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                        Log.d("MAIN", "${year}, ${month+1}, ${day}")
                        dateSelectBtn.setText("${year}, ${month+1}, ${day}")
                    }
                }, year, month, date)

                dlg.show()
            }

            val saveBtn = mAlertDialog.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {

                val healthMemo = mAlertDialog.findViewById<EditText>(R.id.healthMemo)?.text.toString()
                val date = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)?.text.toString()
                val model = DataModel(date,healthMemo)

                // insert
                myRef.push().setValue(model)
                // merge
                //myRef.setValue(model)

                // 창 끄기
                mAlertDialog.dismiss()

            }
        }

    }
}