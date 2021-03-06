package services.authenticator;

import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.concurrent.*;

public class UserSessionCache {


    static final long SESSIONTIME = 5000000000l;   // 60 secs before the session will expire.

    PriorityQueue<Tuple> queue = new PriorityQueue<>();

    ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);

    public synchronized boolean contains(String sessionId) {
         return currentSessions.contains(sessionId);
    }


    static class Holder {
        static UserSessionCache cache = new UserSessionCache();
    }

    public static UserSessionCache getInstance()
    {
        return Holder.cache;
    }

    private UserSessionCache()
    {

    }


    static class Tuple implements Comparable<Tuple> {
        String sessionId;
        long time;
        String userid;

        public Tuple(String sessionId, long time,String userid) {
            this.sessionId=sessionId;
            this.time=time;
            this.userid = userid;
        }

        @Override
        public int compareTo(Tuple tuple) {
            return Long.compare(this.time,tuple.time);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return time == tuple.time &&
                    sessionId.equals(tuple.sessionId) &&
                    userid.equals(tuple.userid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, time, userid);
        }
    }


     class SessionEvictor implements Runnable
    {



        public void run()
        {

            if (queue.isEmpty())
                return;


            while(true) {
                final Tuple remove = queue.peek();

                long curr = System.nanoTime();

                if (curr - remove.time > SESSIONTIME) {

                    evict(remove);
                    queue.remove();
                }
                else
                {
                    break;
                }

            }
        }



    }

    ScheduledFuture<?> handle ;

    private ConcurrentMap<String,Tuple> usersession = new ConcurrentHashMap<>();

    private Set<String> currentSessions = new HashSet<>();

    public synchronized void put(String userid, String sessionId)
    {
        long time = System.nanoTime();

        Tuple tuple = new Tuple(sessionId,time,userid);

        usersession.put(userid,tuple);

        currentSessions.add(sessionId);

        queue.add(tuple);

        if (queue.peek().equals(tuple))
        {
            // adjust the timer since the new task goes to the top. else no need to adjust the task.

            if (handle!=null)
            {
                handle.cancel(true);
            }

            SessionEvictor evictor = new SessionEvictor();
            handle = service.scheduleAtFixedRate(evictor,1,1, TimeUnit.SECONDS);

        }


    }

    public synchronized void remove(String userid)
    {
        Tuple tuple = usersession.remove(userid);
        if (tuple!=null)
        {
            currentSessions.remove(tuple.sessionId);
        }

    }


    synchronized void evict(Tuple tuple)
    {
        System.out.println("Session expired for user  " + tuple.userid);
        remove(tuple.userid);

    }



    public static void main(String[] args) throws Exception  {

        UserSessionCache cache = new UserSessionCache();

        Random random = new Random();

        for (int i=0;i<10;i++) {
            cache.put("User " + i, UUID.randomUUID().toString());
            Thread.sleep(random.nextInt(10) * 1000);

        }

        Thread.sleep(500 * 1000); // to trigger evictions


    }

}
