package SkyWorld;

public class Sacrifice implements Runnable
{
    int get;
    int stage=1;
    int damages;
    boolean redStoneRain;
    boolean inSafePlace;
    @Override
    public void run()
    {
        while (stage==1)
        {
            damages=4;
            try
            {
                System.out.println("现在是红石雨间歇时间，尽快献祭吧！");
                redStoneRain=false;
                Thread.sleep(6000);
                if(stage!=1)
                    break;
                System.out.println("红石雨即将下落！赶紧躲避！");
                Thread.sleep(2000);
                if(stage!=1)
                    break;
                System.out.println("红石雨正在下落！");
                redStoneRain=true;
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
        if (stage==2)
        {
            damages=2;
            redStoneRain=true;
            System.out.println("进入二阶段！为无尽红石雨模式，尽快献祭吧！");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean nextStatue()
    {
        if(get<63)
        {
            get++;
            return true;
        }
        else return false;
    }
}
