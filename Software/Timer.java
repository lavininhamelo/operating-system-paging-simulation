package Software;

import Hardware.CPU;
import Software.Schedulers.RoundRobinScheduler;

import Software.Notification.MonitorRoundRobinTimer;
import Software.Notification.MonitorDispatcherTimer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Responsável por supervisionar o tempo de processamento da CPU.
 */
public class Timer extends Thread {

	/**
	 * Tempo de processamento da CPU.
	 */
	private static int timer;

	/**
	 * Comunicação com a CPU.
	 */
	private CPU cPU;

	/**
	 * Comunicação com o RoundRobinScheduler.
	 */
	private RoundRobinScheduler roundRobinScheduler;

	/**
	 * Comunicação com o Dispatcher.
	 */
	private Dispatcher dispatcher;

	/**
	 * Monitor entre RoundRobinScheduler e Timer.
	 */
	private MonitorRoundRobinTimer monitorRoundRobin;

	/**
	 * Monitor entre Dispatcher e Timer.
	 */
	private MonitorDispatcherTimer monitorDispatcher;

	/**
	 * Monitoramento de status do RoundRobinScheduler.
	 */
	private String statusRoundRobinScheduler;

	public Timer(RoundRobinScheduler roundRobinScheduler, MonitorRoundRobinTimer monitorRoundRobin, MonitorDispatcherTimer monitorDispatcher) {
		this.roundRobinScheduler = roundRobinScheduler;
		this.monitorRoundRobin = monitorRoundRobin;
		this.monitorDispatcher = monitorDispatcher;
		this.statusRoundRobinScheduler = "";
	}

	public void setcPU(CPU cPU) {
		this.cPU = cPU;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void setStatusRoundRobinScheduler(String statusRoundRobinScheduler) {
		this.statusRoundRobinScheduler = statusRoundRobinScheduler;
	}

	/**
	 * Notifica ao Scheduler que o processo foi finalizado.
	 *
	 * O processo pode ser finalizado em duas situações:
	 *
	 * 	- Quando o CPU buster do processo é igual a 0;
	 * 	- Quando o timer excede o time quantum definido pelo scheduler
	 *
	 */
	private void notifyScheduler() {

		synchronized(monitorRoundRobin) {

			monitorRoundRobin.notify();

		}

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Timer informa ao Escalonador Round-Robin de CPU que o processo " + cPU.getProcess().getId()
				+ " atualmente em execução precisa ser retirado da CPU");

	}

	/**
	 * Realiza ciclo de tempo no sistema operacional.
	 *
	 * Para cada ciclo de tempo deverá ser realizado os seguintes processos:
	 *
	 * 	- Processamento da CPU;
	 * 	- Verificação do CPU burst do processo;
	 * 	- Verificação de time quantum em relação ao tempo de processamento da CPU.
	 *
	 *
	 */
	public void run() {

		while (!statusRoundRobinScheduler.equals("concluded")) {

			if (cPU.getProcess() == null) {

				synchronized (monitorDispatcher) {

					try {
						monitorDispatcher.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}

			if (cPU.getProcess() != null) {
				timer--;
				cPU.run();

			}

			if ((cPU.getProcess() != null) && (timer <= 0 || cPU.getProcess().getTb() <= 0)) {

				roundRobinScheduler.setProcess(cPU.getProcess());
				notifyScheduler();
				cPU.setProcess(null);

			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}

		}

	}

}
