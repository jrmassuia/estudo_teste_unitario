package br.ce.wcaquino.servicos;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.runners.ParallelRunner;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    @InjectMocks
    @Spy
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
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(service).obterData();

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        //error.checkThat(locacao.getDataLocacao(), ehHoje());
        //error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        //acao
        service.alugarFilme(usuario, filmes);
    }


    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuario vazio"));
        }

        System.out.println("Forma robusta");
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();

        expected.expect(LocadoraException.class);
        expected.expectMessage("Filme vazio");
        //acao
        service.alugarFilme(usuario, null);

        System.out.println("Forma nova");
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(service).obterData();

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuário2").agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuirNegativacao(usuario)).thenReturn(true);

        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário Negativado!"));
        }

        verify(spc).possuirNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {
        //cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
        List<Locacao> locacoes = Arrays.asList(
                umLocacao()
                        .comUsuario(usuario)
                        .atradada()
                        .agora(),
                umLocacao()
                        .comUsuario(usuario2)
                        .agora(),
                umLocacao()
                        .comUsuario(usuario3)
                        .atradada()
                        .agora());

        when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        //acao
        service.notificarAtrasos();

        //verificao
        verify(emailService).notificarAtraso(usuario);
        verify(emailService).notificarAtraso(usuario3);
        verify(emailService, never()).notificarAtraso(usuario2);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuirNegativacao(usuario)).thenThrow(new Exception("Falha!"));

        //verificacao
        expected.expect(LocadoraException.class);
        expected.expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao() {
        //cenario
        Locacao locacao = umLocacao().agora();

        //acao
        service.prorrogarLocacao(locacao, 3);

        //verificacao
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetornada = argCapt.getValue();

        error.checkThat(locacaoRetornada.getValor(), is(12.0));
        error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
        error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
    }

    @Test
    public void deveCalcularValorLocao() throws Exception {
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Class<LocacaoService> clazz = LocacaoService.class;
        Method method = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
        method.setAccessible(true);
        Double valor = (Double) method.invoke(service, filmes);

        Assert.assertThat(valor, is(4.0));
    }
}