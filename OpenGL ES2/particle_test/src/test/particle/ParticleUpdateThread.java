package test.particle;

public class ParticleUpdateThread extends Thread {
	private final static int MAX_FPS = 24;
	private final static int	FRAME_PERIOD = 1000 / MAX_FPS;	
	CParticleManager pm;
	Boolean bRunning = false;
	public ParticleUpdateThread(CParticleManager p) {
		super();
		pm = p;
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
			pm.update();
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
