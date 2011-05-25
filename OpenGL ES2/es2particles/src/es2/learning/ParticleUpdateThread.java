package es2.learning;

public class ParticleUpdateThread extends Thread {
	private final static int MAX_FPS = 30;
	private final static int	FRAME_PERIOD = 1000 / MAX_FPS;	
	ParticleView curView;
	Boolean bRunning = false;
	public ParticleUpdateThread(ParticleView view) {
		curView = view;
	}
	
	public void SetRunning(Boolean brun){
		bRunning = brun;
	}
	public Boolean GetRuning(){
		return bRunning;
	}
	public void run()
	{
		long lBeginTime;
		long lElapsedTime;
		int iSleepTime;
		while (bRunning) {
			lBeginTime = System.currentTimeMillis();
			curView.mgr.update();
			lElapsedTime = System.currentTimeMillis() - lBeginTime;
			iSleepTime = (int) (FRAME_PERIOD - lElapsedTime);
			if (iSleepTime > 0) {
				try {
					Thread.sleep(iSleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
}
