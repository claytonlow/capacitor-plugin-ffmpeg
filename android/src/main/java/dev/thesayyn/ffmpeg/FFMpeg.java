package dev.thesayyn.ffmpeg;

import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin
public class FFMpeg extends Plugin {

    public void load() {
        // Called when the plugin is first constructed in the bridge
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

        FFmpegSession session = FFmpegKit.execute(args);

        // Unique session id created for this execution
        long sessionId = session.getSessionId();

        // Command arguments as a single string
        String command = session.getCommand();

        // Command arguments
        String[] arguments = session.getArguments();

        // State of the execution. Shows whether it is still running or completed
        SessionState state = session.getState();

        // Return code for completed sessions. Will be null if session is still running or ends with a failure
        ReturnCode returnCode = session.getReturnCode();

        Date startTime = session.getStartTime();
        Date endTime = session.getEndTime();
        long duration = session.getDuration();

        // Console output generated for this execution
        String output = session.getOutput();

        // The stack trace if FFmpegKit fails to run a command
        String failStackTrace = session.getFailStackTrace();

        // The list of logs generated for this execution
        List<com.arthenica.ffmpegkit.Log> logs = session.getLogs();

        // The list of statistics generated for this execution
        List<Statistics> statistics = session.getStatistics();

        
        if (ReturnCode.isSuccess(returnCode)) {
            Log.d(TAG, String.format("WE GOOOOOOOOOD state %s and rc %s.%s", session.getState(), session.getReturnCode(), session.getFailStackTrace()));
            // SUCCESS
            JSObject result = new JSObject();
            result.put("execution_id", "asdfadsf");
            call.success(result);
        } else if (ReturnCode.isCancel(returnCode)) {
        
            // CANCEL
        
        } else {
        
            // FAILURE
            Log.d(TAG, String.format("Command failed with state %s and rc %s.%s", session.getState(), session.getReturnCode(), session.getFailStackTrace()));
        
        }
    }
}
