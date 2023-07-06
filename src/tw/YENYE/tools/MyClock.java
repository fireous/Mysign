package tw.YENYE.tools;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;

public class MyClock extends JLabel {
	private Timer timer;
	public MyClock() {
		setText("10:20:30");
		timer = new Timer();
		timer.schedule(new MyTaskV2(), 0, 1000);
	}
	
	private class MyTask extends TimerTask{
		private DateTimeFormatter dtf ;
		private LocalDateTime now;
		private int i;
		MyTask() {
			this.now = LocalDateTime.now();
			this.dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		}
		@Override
		public void run() {
			setText(dtf.format(now.plusSeconds(i++)));
		}
	}

	private class MyTaskV2 extends TimerTask{
		private Date dt;
		private DateFormat df ;
		MyTaskV2() {
			this.dt = new Date();
			this.df = new SimpleDateFormat("HH:mm:ss");
		}
		@Override
		public void run() {
			dt.setTime(dt.getTime()+1000);
			setText(df.format(dt));
		}
	}
	
}
