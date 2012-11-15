package esercizio.pdm;

import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.packet.Message.Body;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements MessageReceiver {

	enum Stato {
		WAIT_FOR_START, WAIT_FOR_START_ACK, USER_SELECTING, WAIT_FOR_NUMBER_SELECTION, WAIT_FOR_BET, USER_BETTING
	}

	private static final int SHOW_TOAST = 0;

	TextView et1;

	ConnectionManager connection;
	private String nomeMio, pass, nomeAvversario;

	private Stato statoCorrente;
	String TAG = "Indovina.Activity";
	Timer timer = new Timer();
	TimerTask sendStart = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (statoCorrente == Stato.WAIT_FOR_START_ACK) {
				connection.send("START");
			} else {
				Log.d(TAG, "Sending START but the state is " + statoCorrente);
			}
		}
	};

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Main.SHOW_TOAST:
				Toast.makeText(Main.this, msg.getData().getString("toast"),
						Toast.LENGTH_LONG).show();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private String selectedNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		et1 = (TextView) findViewById(R.id.textView4);
		nomeMio = getIntent().getExtras().getString("iltestonelbox");
		pass = getIntent().getExtras().getString("Password");
		nomeAvversario = getIntent().getExtras().getString("iltestonelbox2");
		et1.setText(nomeMio + "   " + nomeAvversario);

		Button btn1 = (Button) findViewById(R.id.button1);
		Button btn2 = (Button) findViewById(R.id.button2);
		Button btn3 = (Button) findViewById(R.id.button3);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				numberSelected(v);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				numberSelected(v);
			}
		});
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				numberSelected(v);
			}
		});

		connection = new ConnectionManager(nomeMio, pass, nomeAvversario, this);

		if (nomeAvversario.hashCode() < nomeMio.hashCode()) {
			// inizio io
			timer.schedule(sendStart, 1000, 20000);
			changeState(Stato.WAIT_FOR_START_ACK);
		} else {
			// inizia lui, io aspetto il pacchetto
			changeState(Stato.WAIT_FOR_START);
		}

	}

	@Override
	public void receiveMessage(String body) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Ricevuto messaggio: " + body);
		if (body.equals("START")) {
			if (statoCorrente == Stato.WAIT_FOR_START) {
				// mando l'ack indietro
				connection.send("STARTACK");
				Message osmsg = handler.obtainMessage(Main.SHOW_TOAST);
				Bundle b = new Bundle();
				b.putString("toast", "Scegli un numero");
				osmsg.setData(b);
				handler.sendMessage(osmsg);
				changeState(Stato.WAIT_FOR_NUMBER_SELECTION);
			} else {
				Log.e(TAG, "Ricevuto START ma lo stato è " + statoCorrente);
			}
		} else if (body.equals("STARTACK")) {
			if (statoCorrente == Stato.WAIT_FOR_START_ACK) {
				changeState(Stato.USER_SELECTING);
				timer.cancel();
				Message osmsg = handler.obtainMessage(Main.SHOW_TOAST);
				Bundle b = new Bundle();
				b.putString("toast", "Il gioco è cominciato");
				osmsg.setData(b);
				handler.sendMessage(osmsg);
			} else {
				Log.e(TAG, "Ricevuto START ma lo stato è " + statoCorrente);
			}
		} else if (body.startsWith("SELECTED:")) {
			if (statoCorrente == Stato.WAIT_FOR_NUMBER_SELECTION) {

				selectedNumber = body.split(":")[1];
				Message osmsg = handler.obtainMessage(Main.SHOW_TOAST);
				Bundle b = new Bundle();
				b.putString("toast", "Indovina il numero");
				osmsg.setData(b);
				handler.sendMessage(osmsg);
				changeState(Stato.USER_BETTING);
			} else {
				Log.e(TAG, "Ricevuto SELECTED ma lo stato è " + statoCorrente);
			}
		} else if (body.startsWith("BET")) {
			if (statoCorrente == Stato.WAIT_FOR_BET) {
				String result = body.split(":")[1];
				Message osmsg = handler.obtainMessage(Main.SHOW_TOAST);
				Bundle b = new Bundle();
				if (result.equals("Y")) {
					b.putString("toast",
							"Hai perso, il tuo avversario ha indovinato");
				} else {
					b.putString("toast",
							"Hai vinto, il tuo avversario ha sbagliato");
				}
				osmsg.setData(b);
				handler.sendMessage(osmsg);
				changeState(Stato.WAIT_FOR_NUMBER_SELECTION);

			} else {
				Log.e(TAG, "Ricevuto SELECTED ma lo stato è " + statoCorrente);
			}
		}

	}

	public void numberSelected(View v) {
		Button b = (Button) v;
		String B = b.getText().toString();
		if (statoCorrente == Stato.USER_SELECTING) {
			connection.send("SELECTED:" + B);
			changeState(Stato.WAIT_FOR_BET);
		} else if (statoCorrente == Stato.USER_BETTING) {
			if (B.equals(selectedNumber)) {
				connection.send("BET:Y");
				Toast.makeText(Main.this,
						"Bravo hai indovinato, ora tocca a te",
						Toast.LENGTH_LONG).show();
			} else {
				connection.send("BET:N");
				Toast.makeText(Main.this,
						"Peccato non hai indovinato, ora tocca a te",
						Toast.LENGTH_LONG).show();
			}
			changeState(Stato.USER_SELECTING);
		}
	}

	void changeState(Stato nuovoStato) {
		Log.d(TAG, "Transizione di stato: " + statoCorrente + "--->"
				+ nuovoStato);
		statoCorrente = nuovoStato;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		connection.close();
		timer.cancel();
	}

}