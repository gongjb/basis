package com.hk.xnet.task;


import com.hk.xnet.ResponseDataListener;
import com.lzy.okgo.utils.OkLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestTaskManages {
    private LinkedList<TaskGen> mTaskLinked = new LinkedList<>();
    private static RequestTaskManages instance;
    private RequestTaskManages(){}

    /**
     *
     * @return
     */
    public static RequestTaskManages getInstance() {
        if (instance == null) {
            // 加锁
            synchronized (RequestTaskManages.class) {
                if (instance == null) {
                    instance = new RequestTaskManages();
                }
            }
        }
        return instance;
    }

    /**
     * 开始任务
     * @param taskId
     * @param path
     */
    public void startTask(final long taskId, final String path, ResponseDataListener responseDataListener) {
        // 创建一个新任务
        TaskGen newTask = new TaskGen(taskId, path, false, responseDataListener);
        newTask.startTask();
        mTaskLinked.add(newTask);
    }

    /**
     * 根据任务id 找到任务
     * @param taskId
     * @return
     */
    public TaskGen genTask(final long taskId) {
        List<TaskGen> list = mTaskLinked.stream().filter(taskBean -> taskId == taskBean.getTaskId()).collect(Collectors.toList());
        if (list !=null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 停止任务(请求任务完成调用)
     * @param taskId
     */
    public void stopTask(final long taskId) {
       TaskGen taskGen = genTask(taskId);
       if (taskGen != null) {
           taskGen.setCompleteTheRequest(true);
           if (mTaskLinked.contains(taskGen)) {
               mTaskLinked.remove(taskGen);
               OkLogger.d("TaskGen: 停止任务 从集合中移除任务..." + taskGen.getPath());
           } else {
               OkLogger.d("TaskGen: 停止任务 集合中未找到任务" + taskGen.getPath());
           }
       } else {
           OkLogger.d("TaskGen: 停止任务 未找到任务:" + taskId);
       }
    }

    /**
     * 生成id
     * @return
     */
    public long genId() {
        if (mTaskLinked.isEmpty()) {
            return 0;
        } else {
          return (mTaskLinked.getLast().getTaskId() + 1);
        }
    }
}
