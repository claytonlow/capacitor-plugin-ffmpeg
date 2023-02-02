package dev.thesayyn.ffmpeg;

import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin
public class FFMpeg extends Plugin {

    public FFMpeg() {
        FFmpegKitConfig.enableStatisticsCallback(statistics -> {
            JSObject stats = new JSObject();
            stats.put("execution_id", statistics.getExecutionId());
            stats.put("bitrate", statistics.getBitrate());
            stats.put("size", statistics.getSize());
            stats.put("speed", statistics.getSpeed());
            stats.put("time", statistics.getTime());
            stats.put("video_fps", statistics.getVideoFps());
            stats.put("video_frame_number", statistics.getVideoFrameNumber());
            stats.put("video_quality", statistics.getVideoQuality());
            notifyListeners("statistic", stats);
        });
        FFmpegKitConfig.enableLogCallback(message -> {
            JSObject entry = new JSObject();
            entry.put("execution_id", message.getExecutionId());
            entry.put("level", message.getLevel());
            entry.put("text", message.getText());
            notifyListeners("message", entry);
        });
    }

    @PluginMethod
    public void run(PluginCall call) {
        if (! call.hasOption("args") ) {
            call.error("args property is missing.");
            return;
        }

        String args = call.getString("args");

        // FFmpegKit.executeAsync(args, (executionId, returnCode) -> {
        //     if (returnCode == Config.RETURN_CODE_SUCCESS) {
        //         JSObject result = new JSObject();
        //         result.put("execution_id", executionId);
        //         call.success(result);
        //     } else {
        //         call.error("process has failed.", String.valueOf(returnCode), new Exception("process has failed."));
        //     }
        // });

        FFmpegKit.executeAsync(args, new FFmpegSessionCompleteCallback() {

            @Override
            public void apply(FFmpegSession session) {
                SessionState state = session.getState();
                ReturnCode returnCode = session.getReturnCode();
        
                // CALLED WHEN SESSION IS EXECUTED
        
                if (returnCode == FFmpegKitConfig.RETURN_CODE_SUCCESS) {
                    JSObject result = new JSObject();
                    result.put("execution_id", executionId);
                    call.success(result);
                } else {
                    call.error("process has failed.", String.valueOf(returnCode), new Exception("process has failed."));
                }
                Log.d(TAG, String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.getFailStackTrace()));
            }
        }, new LogCallback() {
        
            @Override
            public void apply(com.arthenica.ffmpegkit.Log log) {
        
                // CALLED WHEN SESSION PRINTS LOGS
        
            }
        }, new StatisticsCallback() {
        
            @Override
            public void apply(Statistics statistics) {
        
                // CALLED WHEN SESSION GENERATES STATISTICS
        
            }
        });
        

    }
}
