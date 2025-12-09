package br.ce.wcaquino.suites;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//Verificar o uso da suite pois pode executar duas vezes se integração
// continua quando se executa o pacote 'test'
//@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {

    @BeforeClass
    public static void before() {
        System.out.printf("before");
    }

    @AfterClass
    public static void after() {
        System.out.printf("after");
    }
}
