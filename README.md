# JavaUtils
Java functions and classes that that been useful in more than one project.

# Using thread pools

    import org.s2n.ddt.util.threads.PoolOptions;
    import org.s2n.ddt.util.threads.DdtPools;

    //One time init
    public static final String PROCESS_POOL = "MyBus_Proc";
    PoolOptions options = new PoolOptions(4, 4);//could come from config
    DdtPools.initPool(PROCESS_POOL, options);
    
    // make your runnable class, and offer it to the pool
    org.s2n.ddt.util.threads.DdtPools.offer(PROCESS_POOL, this);
    //if the pool is not initialized will be processed by org.s2n.ddt.util.threads.DdtPools.DEFAULT_POOL so it will get processed
