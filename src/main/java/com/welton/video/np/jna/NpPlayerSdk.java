package com.welton.video.np.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Structure;

public interface NpPlayerSdk extends Library {
	// np_netsdk np_fileplayer np_filesdk np_netsdk_pvg671
	NpPlayerSdk INSTANCE = (NpPlayerSdk) Native.load((Platform.isWindows() ? "np_playsdk" : "c"), NpPlayerSdk.class);

	int NP_PLAY_CreatePlayer(int[] playHD, int hwnd, int streamType, VidioDecodeCallback fnOnDecoder, String userParam);

	public interface VidioDecodeCallback extends Callback {
		public void handle(int playHD, String buf, int len, NPPlayFrameInfo fInfo, String userParam);
	}

	public class NPPlayFrameInfo extends Structure {
		long width;
		long height;
		long type;
		int pts;
	}

	int NP_PLAY_ReleasePlayer(int playHD);
}