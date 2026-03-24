package io.github.ffakira.moparscape.client.lifecycle;

public final class LoginProtocolHandler {

    private LoginProtocolHandler() {
    }

    public static boolean shouldShowConnectingUi(boolean reconnecting)
    {
        return !reconnecting;
    }

    public static String[] resolveLoginError(int responseCode)
    {
        if(responseCode == 3)
            return new String[] {
                "", "Invalid username or password."
            };
        if(responseCode == 4)
            return new String[] {
                "Your account has been disabled.", "Please check your message-centre for details."
            };
        if(responseCode == 5)
            return new String[] {
                "Your account is already logged in.", "Try again in 60 secs..."
            };
        if(responseCode == 6)
            return new String[] {
                "MoparScape has been updated!", "Please reload this page."
            };
        if(responseCode == 7)
            return new String[] {
                "This world is full.", "Please use a different world."
            };
        if(responseCode == 8)
            return new String[] {
                "Unable to connect.", "Login server offline."
            };
        if(responseCode == 9)
            return new String[] {
                "Login limit exceeded.", "Too many connections from your address."
            };
        if(responseCode == 10)
            return new String[] {
                "Unable to connect.", "Bad session id."
            };
        if(responseCode == 11)
            return new String[] {
                "Login server rejected session.", "Please try again."
            };
        if(responseCode == 12)
            return new String[] {
                "You need a members account to login to this world.", "Please subscribe, or use a different world."
            };
        if(responseCode == 13)
            return new String[] {
                "Could not complete login.", "Please try using a different world."
            };
        if(responseCode == 14)
            return new String[] {
                "The server is being updated.", "Please wait 1 minute and try again."
            };
        if(responseCode == 16)
            return new String[] {
                "Login attempts exceeded.", "Please wait 1 minute and try again."
            };
        if(responseCode == 17)
            return new String[] {
                "You are standing in a members-only area.", "To play on this world move to a free area first"
            };
        if(responseCode == 20)
            return new String[] {
                "Invalid loginserver requested", "Please try using a different world."
            };
        return null;
    }

    public static String resolveTransferCountdownMessage(int seconds)
    {
        return "Your profile will be transferred in: " + seconds + " seconds";
    }

    public static void sleepSilently(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(Exception _ex) { }
    }

    public static boolean shouldRetryAfterNoResponse(int initialResponseCode, int retryCount)
    {
        return initialResponseCode == 0 && retryCount < 2;
    }

    public static String[] resolveNoResponseError(int initialResponseCode)
    {
        if(initialResponseCode == 0)
            return new String[] {
                "No response from loginserver", "Please wait 1 minute and try again."
            };
        return new String[] {
            "No response from server", "Please try using a different world."
        };
    }
}
