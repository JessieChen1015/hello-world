package cloud.simple.hello;

/** 
 * ClassName: TraditionalThreadSynchronized <br/> 
 * Function: 创建两个线程，了解线程同步问题. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2016年12月16日 上午10:09:46 <br/> 
 * 
 * @author admin 
 * @version  
 */  
public class TraditionalThreadSynchronized {
	//创建两个线程，执行同一个对象的输出方法
	public static void main(String[] args){
		final Outputter output = new Outputter();
		//第一个线程
		new Thread(){
			public void run(){
				output.outPut("zhangsan");
				//线程同步
//				output.outPutSynch("zhangsan");
				//错误示例
//				output.outPutSynchError("zhangsan");
				//将synchronized加在需要互斥的方法上
//				output.outPutMethod("zhangsan");
			}
		}.start();
		
		//第二个线程
		new Thread(){
			public void run(){
				output.outPut("lisi");
				//线程同步
//				output.outPutSynch("lisi");
				//错误示例
//				output.outPutSynchError("lisi");
				//将synchronized加在需要互斥的方法上
//				output.outPutMethod("lisi");
			}
		}.start();
	}
}

/**
 * 线程输出
 * */
class Outputter{
	
	//1、线程输出
	public void outPut(String name){
		//为了保证对name的输出不是一个原子操作，这里逐个输出name的每个字符
		for(int i=0;i<name.length();i++){
			System.out.print(name.charAt(i));
			try {
				Thread.sleep(10);//线程休眠，减慢线程
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//输出结果:zlhiasnigsan
	//显然输出的字符串被打乱了，我们期望的输出结果是zhangsanlisi，这就是线程同步问题，
	//我们希望output方法被一个线程完整的执行完之后再切换到下一个线程，Java中使用synchronized保证一段代码在多线程执行时是互斥的
	
	//2、线程同步，加锁
	public void outPutSynch(String name){
		//这把锁必须是需要互斥的多个线程间的共享对象--name
		synchronized (this) {
			//为了保证对name的输出不是一个原子操作，这里逐个输出name的每个字符
			for(int i=0;i<name.length();i++){
				System.out.print(name.charAt(i));
				try {
					Thread.sleep(10);//线程休眠，减慢线程
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//输出结果:zhangsanlisi
	
	//3、错误示例，未共享对象,每次进入output方法都会创建一个新的lock，这个锁显然每个线程都会创建，没有意义。
	public void outPutSynchError(String name){
		//这把锁必须是需要互斥的多个线程间的共享对象，像下面的代码是没有意义的。
		Object lock = new Object();  
		synchronized (lock) {
			//为了保证对name的输出不是一个原子操作，这里逐个输出name的每个字符
			for(int i=0;i<name.length();i++){
				System.out.print(name.charAt(i));
				try {
					Thread.sleep(10);//线程休眠，减慢线程
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//输出结果:zlhiasnigsan
	
	//4、将synchronized加在需要互斥的方法上。
	public synchronized void outPutMethod(String name){
		//线程输出方法
		for(int i=0;i<name.length();i++){
			System.out.print(name.charAt(i));
		}
	}
	//输出结果:zhangsanlisi
	//这种方式就相当于用this锁住整个方法内的代码块，
	//如果用synchronized加在静态方法上，
	//就相当于用××××.class锁住整个方法内的代码块。
	//使用synchronized在某些情况下会造成死锁。
	//使用synchronized修饰的方法或者代码块可以看成是一个原子操作。
}
