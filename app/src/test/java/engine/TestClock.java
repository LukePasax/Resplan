package engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import daw.engine.*;

class TestClock {

	@Test
	void testClockCreation() {
		RPClock clock = new Clock();
		assertEquals(0, clock.getTime());
		assertEquals(0, clock.getStep());
	}
	
	@Test
	void testClockTime() {
		RPClock clock = new Clock();
		Double time = 423784863.343234;
		clock.setTime(time);
		assertEquals(clock.timeToClockSteps(time), clock.getStep());
		assertEquals(clock.roundToExistingClockTime(time), clock.getTime());
		time = clock.getClockMaxTime();
		clock.setTime(time);
		assertEquals(clock.timeToClockSteps(time), clock.getStep(), "Tested with Clock Max Time");
		assertEquals(clock.roundToExistingClockTime(time), clock.getTime(), "Tested with Clock Max Time");
	}
	
	@Test
	void testClockStep() throws ClockException {
		RPClock clock = new Clock();
		long steps = 200;
		for(int i = 0; i<steps; i++) {
			clock.step();	
		}
		assertEquals(steps, clock.getStep());
	}
	
	@Test
	void testClockReset() throws ClockException {
		RPClock clock = new Clock();
		for(int i = 0; i<25; i++) {
			clock.step();
		}
		clock.reset();
		assertEquals(0, clock.getTime());
		assertEquals(0, clock.getStep());
	}
	
	@Test
	void testClockMaxTimeReached() throws ClockException {
		RPClock clock = new Clock();
		Double time = clock.getClockMaxTime();
		clock.setTime(time);
		assertEquals(clock.getClockMaxTime(), clock.getTime());
		assertEquals(clock.roundToExistingClockTime(time), clock.getTime());
		assertThrows(ClockException.class, ()->clock.step());
	}
	
	@Test
	void testClock1000RandomTimes() {
		RPClock clock = new Clock();
		for(int i=0; i<1000; i++) {
			Double time = Math.random()*clock.getClockMaxTime();
			clock.setTime(time);
			assertEquals(clock.timeToClockSteps(time), clock.getStep());
			assertEquals(clock.roundToExistingClockTime(time), clock.getTime());
		}
	}
	
	@Test
	void testAllDifferentTimes() throws ClockException {
		RPClock clock = new Clock();
		Long rand = Double.valueOf(Math.random()*clock.getClockMaxStep()).longValue();
		for(long i=rand; i<rand+10000 && i<clock.getClockMaxStep(); i++) {
			Double oldTime = clock.getTime();
			clock.step();
			assertTrue(clock.getTime()>oldTime);
		}
	}

}
