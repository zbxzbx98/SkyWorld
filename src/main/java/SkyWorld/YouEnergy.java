package SkyWorld;

import java.util.Random;

public class YouEnergy implements Runnable
{
    YouSelf you;
    Sacrifice sa;
    int type;
    public YouEnergy(Sacrifice sa,YouSelf you)
    {
        type=1;
        this.sa=sa;
        this.you=you;
    }

    @Override
    public void run()
    {
        Random r =new Random();
        try
        {
            while (true)
            {
                if (type == 1)
                {
                    if(sa.inSafePlace)
                    {
                        you.nowEnergy+=0.1;
                        you.black=false;
                        if(you.nowEnergy> you.maxEnergy)
                            you.nowEnergy= you.maxEnergy;
                        Thread.sleep(100);
                    }
                    else if(sa.redStoneRain)
                    {
                        if(sa.stage==1)
                        {
                            if(r.nextInt(100)>40)
                            {
                                takeDamages();
                            }
                            Thread.sleep(500);
                        }
                        else
                        {
                            int i =r.nextInt(100);
                            if(i>70)
                            {
                                takeDamages();
                            }
                            else if(i>40&&you.black)
                            {
                                System.out.println("你被红石砸中了，但没有掉翼！");
                            }
                            Thread.sleep(400);
                        }
                    }
                    else
                    {
                        Thread.sleep(500);
                    }
                }
                else if(type==-1)
                {
                    return;
                }
            }
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void takeDamages()
    {
        you.nowEnergy-= sa.damages;
        if(you.nowEnergy<=0)
        {
            you.nowEnergy=0;
        }
        System.out.println("你被红石砸中了！掉了"+sa.damages+"点能量，现在还剩"+String.format("%.2f",you.nowEnergy)+"点能量");
        if(you.nowEnergy==0)
        {
            if(!you.black)
            {
                System.out.println("你被砸黑了！掉了"+you.lostLightWing()+"个光翼！现在还剩"+you.wings_of_light+"个");
                you.black=true;
            }
            else
            {
                System.out.println("你又被砸掉了"+you.lostLightWing()+"个光翼！现在还剩"+you.wings_of_light+"个");
            }
        }
    }
}
