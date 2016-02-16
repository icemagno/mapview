package br.com.cmabreu;

public interface IKeyReaderObserver {
	void notify( String key );
	void whenIdle();
}
