package org.s2n.ddt.util.http;

import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.s2n.ddt.util.threads.DdtPools;

/**
 * Post back info helper. With fluent API. See AgentPools for usage sample
 */
public class PostBackAsync implements Runnable {
    private static final Logger logger = LogManager.getLogger(PostBackAsync.class);

    // private List<Serializable> postBakckObjects = new ArrayList<Serializable>(1);
    private Serializable[] postBakckObjects = null;
    private String objTyp;
    private String logMsg;
    private Level logLvl;
    private Level logLvlOk;
    private HttpData hDat;

    public PostBackAsync() {

    }

    public PostBackAsync(String objTypeMsg, String logmsg, String slogLvl, HttpData hDat, Serializable... postBakckObjectsp) {
        super();
        postBakckObjects = postBakckObjectsp;
        this.hDat = hDat;
        this.objTyp = objTypeMsg;
        this.logMsg = logmsg;
        setLogLvl(slogLvl);
    }

    public PostBackAsync setLogLvlOk(Level l) {
        logLvlOk = l;
        return this;
    }

    public void start(String poolNm, Boolean asyncCheck) {
        if (asyncCheck == null || asyncCheck) {
            if (poolNm == null || poolNm.length() == 0) {
                poolNm = DdtPools.DEFAULT_POOL;
            }
            DdtPools.offer(poolNm, this);

        } else {
            run();
        }
    }

    public String getObjType() {
        return objTyp;
    }

    public PostBackAsync setObjType(String msg) {
        this.objTyp = msg;
        return this;
    }

    public String getLogMsg() {
        return logMsg;
    }

    public PostBackAsync setLogMsg(String logmsg) {
        this.logMsg = logmsg;
        return this;
    }

    public Level getLogLvl() {
        return logLvl;
    }

    public PostBackAsync setLogLvl(String slogLvl) {
        if (slogLvl == null) {
            slogLvl = "";
        }
        if ("info".equalsIgnoreCase(slogLvl)) {
            this.logLvl = Level.INFO;
        } else {
            this.logLvl = Level.WARN;
        }
        return this;
    }

    public PostBackAsync setLogLvlOk(String slogLvl) {
        if (slogLvl == null) {
            slogLvl = "";
        }
        if ("info".equalsIgnoreCase(slogLvl)) {
            this.logLvlOk = Level.INFO;
        } else {
            this.logLvlOk = Level.WARN;
        }
        return this;
    }

    public PostBackAsync setHttpParams(HttpData hDat) {
        this.hDat = hDat;
        return this;
    }

    public PostBackAsync add(Serializable postBakckObject) {
        if (postBakckObject != null) {
            Serializable[] aa = null;
            if (postBakckObjects == null) {
                aa = new Serializable[1];
            } else {
                aa = new Serializable[postBakckObjects.length + 1];
                System.arraycopy(postBakckObjects, 0, aa, 0, aa.length - 1);
            }
            aa[aa.length - 1] = postBakckObject;
            postBakckObjects = aa;
        }
        return this;
    }

    public void run() {
        try {
            if (hDat != null && hDat.getUrl() != null) {
                // Serializable[] objs = postBakckObjects.toArray(new Serializable[postBakckObjects.size()]);
                Object rtn = NetSend.sendObjects(objTyp, hDat, postBakckObjects);
                if (logLvlOk == null) {
                    logLvlOk = logLvl;
                }
                logger.log(logLvlOk, logMsg + " " + rtn);
            } else {
                logger.log(logLvl, logMsg + " Url null, not posting back");
            }
        } catch (Exception e) {
            logger.warn("logMsg :" + logMsg + ", hDat :" + hDat);
            logger.warn("Post back error :" + e, e);
        }
    }
}
