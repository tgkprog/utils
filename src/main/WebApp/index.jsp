Create thread pool and test it
org.s2n.ddt.util.threads offer
<%

org.s2n.ddt.util.threads.DddtPools.offer("a", new TestJob(10,4));
%>