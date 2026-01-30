package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;

public interface SPCService {

    public boolean possuirNegativacao(Usuario usuario) throws Exception;
}
