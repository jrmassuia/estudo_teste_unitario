package br.ce.wcaquino.servicos;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceComPowerMockTest {

    @InjectMocks
    private LocacaoService service;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private SPCService spc;
    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(LocacaoService.class);
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//        error.checkThat(locacao.getDataLocacao(), ehHoje());
//        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
    }


    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        PowerMockito.whenNew(Date.class)
                .withNoArguments()
                .thenReturn(DataUtils.obterData(29, 4, 2017));


        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
//        PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();

        PowerMockito.verifyStatic(Mockito.times(2));
        Calendar.getInstance();
    }


    @Test
    public void deveAlugarFilmeSemCalcularValor() throws Exception {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        PowerMockito.doReturn(1.0)
                .when(LocacaoService.class, "calcularValorLocacao", filmes);

        Locacao locacao = service.alugarFilme(usuario, filmes);

        Assert.assertThat(locacao.getValor(), is(1.0));
        PowerMockito.verifyPrivate(LocacaoService.class).invoke("calcularValorLocacao", filmes);
    }

    @Test
    public void deveCalcularValorLocao() throws Exception {
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Double valor = Whitebox.invokeMethod(LocacaoService.class, "calcularValorLocacao", filmes);

        Assert.assertThat(valor, is(4.0));
    }
}