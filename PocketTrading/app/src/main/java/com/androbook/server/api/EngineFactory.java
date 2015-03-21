package com.androbook.server.api;

import com.androbook.server.matcher.Engine;
import com.androbook.server.mock.EngineMock;

public class EngineFactory {
	
	public static ITradingEngine getEngine(boolean loopback) {
		if (loopback) return new EngineMock();
		return new Engine();
	}

}
