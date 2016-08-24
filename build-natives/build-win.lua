solution "steamworks4j"
	configurations { "ReleaseDLL" }
	platforms { "x32", "x64" }

	project "steamworks4j"

		kind "SharedLib"
		language "C++"

		files { "../java-wrapper/src/main/native/**.h", "../java-wrapper/src/main/native/**.cpp" }
		includedirs {
			"../java-wrapper/src/main/native",
			"../java-wrapper/src/main/native/include/jni",
			"../java-wrapper/src/main/native/include/jni/win32",
			"../sdk/public",
			"../sdk/public/steam" }

		defines { "NDEBUG", "WINDOWS" }
		flags { "Optimize", "StaticRuntime" }
		buildoptions { "/wd4244", "/wd4800", "/wd4996" }

		filter "platforms:x32"
			libdirs { "../sdk/redistributable_bin" }
			links { "steam_api" }

		filter "platforms:x64"
			libdirs { "../sdk/redistributable_bin/win64" }
			links { "steam_api64" }

