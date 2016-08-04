package com.hdsc.edog;

import com.hdsc.edog.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class SetFeedbackActivity extends Activity implements OnClickListener {

	EditText content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_yjfk);
		TuzhiApplication.addActivity(this);
		initViews();
		
	}

	private void initViews(){
		findViewById(R.id.set_yjfk_cancel).setOnClickListener(this);
		findViewById(R.id.set_yjfk_ok).setOnClickListener(this);
		content = (EditText) findViewById(R.id.set_yjfk_content);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.set_yjfk_cancel:
				finish();
				break;
			case R.id.set_yjfk_ok:
				String cont = content.getText().toString();
				if(cont == null || "".equals(cont)){
					Toast.makeText(this, R.string.send_content_noempty, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, content.getText().toString(), Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}
	
}
