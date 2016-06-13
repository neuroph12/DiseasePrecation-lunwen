/**
 * Created by novas on 16/5/27.
 */
class myrunnable implements Runnable
{
    String current;
    String next;
    public myrunnable(String current,String next)
    {
        this.current=current;
        this.next=next;
    }
    @Override
    public void run() {
        for(int i=0;i<10;i++)
        {
            synchronized (current)
            {
                synchronized (next)
                {
                    System.out.println(current);
                    next.notify();
                }
              //  System.out.println(next+" 释放锁");
                try {
                    current.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
public class ThreadTest
{


    public static void main(String[] args)
    {
        String A="A";
        String B="B";
        String C="C";
        myrunnable AA=new myrunnable(A,B);
        myrunnable BB=new myrunnable(B,C);
        myrunnable CC=new myrunnable(C,A);
        new Thread(AA).start();
        new Thread(BB).start();
        new Thread(CC).start();
    }
}
