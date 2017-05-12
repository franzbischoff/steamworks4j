#include "SteamMatchmakingPingResponse.h"
#include "SteamMatchmakingGameServerItem.h"

SteamMatchmakingPingResponse::SteamMatchmakingPingResponse(JNIEnv* env, jobject callback)
	: SteamCallbackAdapter(env, callback) {

}

SteamMatchmakingPingResponse::~SteamMatchmakingPingResponse() {

}

void SteamMatchmakingPingResponse::ServerResponded(gameserveritem_t &server) {
	invokeCallback({
		jobject item = convertGameServerItem(env, server);
		callVoidMethod(env, "serverResponded",
					   "(Lcom/codedisaster/steamworks4j/SteamMatchmakingGameServerItem;)V", item);
	});
}

void SteamMatchmakingPingResponse::ServerFailedToRespond() {
	invokeCallback({
		callVoidMethod(env, "serverFailedToRespond", "()V");
	});
}