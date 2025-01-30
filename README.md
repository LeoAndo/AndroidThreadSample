# development enviroment
```
Android Studio Meerkat Feature Drop | 2024.3.2 Nightly 2025-01-29
```

# memo

An experiment on thread management while an Android app is running. <br>
The results of the investigation are written in the comments of each Activity. <br>
The results will be organized later and compiled in the README. <br>

<img src ="./memo.png" width="50%">

# environment: Emulator Medium Phone API 35

```shell
adb shell cat /proc/sys/kernel/pid_max
```

You will get the following output, where `32768` is the maximum number of processes(my application).

```shell
32768
````

<img src ="./img.png" width="50%">

# STEP1: Find the process ID of a running app

run the following adb command.

```shell
adb shell ps | grep com.example.androidthreadsample
```

You will get the following output, where `26152` is the process ID.

```shell
u0_a232      26152   326   16455780 144344 0                   0 S com.example.androidthreadsample
```

# STEP2：Check the number of running threads by specifying the process ID

run the following adb command.

```shell
adb shell ps -T | grep 26152
```

The output will look like this. In this case, `pool-3-thread-1` is the running thread.<br>
`pool-x-thread-x` represents a thread in the thread pool.<br>

```shell
u0_a232      26152 26152   326   16455780 144344 0                   0 S oidthreadsample
u0_a232      26152 26154   326   16455780 144344 0                   0 S Signal Catcher
u0_a232      26152 26155   326   16455780 144344 0                   0 S perfetto_hprof_
u0_a232      26152 26156   326   16455780 144344 0                   0 S ADB-JDWP Connec
u0_a232      26152 26157   326   16455780 144344 0                   0 S Jit thread pool
u0_a232      26152 26158   326   16455780 144344 0                   0 S HeapTaskDaemon
u0_a232      26152 26159   326   16455780 144344 0                   0 S ReferenceQueueD
u0_a232      26152 26160   326   16455780 144344 0                   0 S FinalizerDaemon
u0_a232      26152 26161   326   16455780 144344 0                   0 S FinalizerWatchd
u0_a232      26152 26162   326   16455780 144344 0                   0 S binder:26152_1
u0_a232      26152 26163   326   16455780 144344 0                   0 S binder:26152_2
u0_a232      26152 26164   326   16455780 144344 0                   0 S binder:26152_3
u0_a232      26152 26166   326   16455780 144344 0                   0 S Profile Saver
u0_a232      26152 26167   326   16455780 144344 0                   0 S RenderThread
u0_a232      26152 26170   326   16455780 144344 0                   0 S SurfaceSyncGrou
u0_a232      26152 26172   326   16455780 144344 0                   0 S binder:26152_4
u0_a232      26152 26173   326   16455780 144344 0                   0 S hwuiTask0
u0_a232      26152 26174   326   16455780 144344 0                   0 S hwuiTask1
u0_a232      26152 26175   326   16455780 144344 0                   0 S binder:26152_2
u0_a232      26152 26181   326   16455780 144344 0                   0 S pool-3-thread-1
u0_a232      26152 26184   326   16455780 144344 0                   0 S binder:26152_5
```
