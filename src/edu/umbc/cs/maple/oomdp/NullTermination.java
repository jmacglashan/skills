package edu.umbc.cs.maple.oomdp;

public class NullTermination extends TerminalFunction {

	public NullTermination() {
	}

	public NullTermination(Domain domain) {
		super(domain);
	}

	@Override
	public boolean isTerminal(State s) {
		return false;
	}

}
