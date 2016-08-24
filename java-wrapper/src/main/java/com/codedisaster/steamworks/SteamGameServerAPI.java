package com.codedisaster.steamworks;

/**
 *
 * @author Francisco "Franz" Bischoff
 */
public class SteamGameServerAPI {

	public enum ServerMode {
		Invalid,
		NoAuthentication,
		Authentication,
		AuthenticationAndSecure
	}

	static public boolean isRunning = false;

	static public boolean init(int ip, short steamPort, short gamePort, short queryPort,
							   ServerMode serverMode, String versionString) {

		return init(null, ip, steamPort, gamePort, queryPort, serverMode, versionString);
	}

	static public boolean init(String pathToNativeLibraries,
							   int ip, short steamPort, short gamePort, short queryPort,
							   ServerMode serverMode, String versionString) {

		boolean fromJar = pathToNativeLibraries == null || pathToNativeLibraries.endsWith(".jar");

		isRunning = SteamSharedLibraryLoader.extractAndLoadLibraries(fromJar, pathToNativeLibraries);

		isRunning = isRunning && nativeInit(ip, steamPort, gamePort, queryPort, serverMode.ordinal(), versionString);

		return isRunning;
	}

	static public void shutdown() {
		isRunning = false;
		nativeShutdown();
	}

	static public SteamID getSteamID() {
		return new SteamID(nativeGetSteamID());
	}

	// @off

	/*JNI
		 #include <steam_gameserver.h>

		 static JavaVM* staticVM = 0;
	*/

	static private native boolean nativeInit(int ip, short steamPort, short gamePort, short queryPort,
											 int serverMode, String versionString); /*

		 if (env->GetJavaVM(&staticVM) != 0) {
			 return false;
		 }

		 return SteamGameServer_Init(ip, steamPort, gamePort, queryPort,
			static_cast<EServerMode>(serverMode), versionString);
	*/

	static private native void nativeShutdown(); /*
		SteamGameServer_Shutdown();
	*/

	static public native void runCallbacks(); /*
		SteamGameServer_RunCallbacks();
	*/

	static public native boolean isSecure(); /*
		return SteamGameServer_BSecure();
	*/

	static private native long nativeGetSteamID(); /*
		return SteamGameServer_GetSteamID();
	*/

	static native long getSteamGameServerPointer(); /*
		return (long) SteamGameServer();
	*/

	static native long getSteamGameServerNetworkingPointer(); /*
		return (long) SteamGameServerNetworking();
	*/

	static native long getSteamGameServerStatsPointer(); /*
		return (long) SteamGameServerStats();
	*/

}
