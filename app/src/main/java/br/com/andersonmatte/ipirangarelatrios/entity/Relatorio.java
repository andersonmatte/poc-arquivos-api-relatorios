package br.com.andersonmatte.ipirangarelatrios.entity;

public class Relatorio {

    private Estabelecimento estabelecimento;
    private String autorizacao;
    private String codigoVip;
    private String dataEmissao;
    private String nsu;
    private String smartPos;
    private Transacao transacao;

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public String getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(String autorizacao) {
        this.autorizacao = autorizacao;
    }

    public String getCodigoVip() {
        return codigoVip;
    }

    public void setCodigoVip(String codigoVip) {
        this.codigoVip = codigoVip;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getNsu() {
        return nsu;
    }

    public void setNsu(String nsu) {
        this.nsu = nsu;
    }

    public String getSmartPos() {
        return smartPos;
    }

    public void setSmartPos(String smartPos) {
        this.smartPos = smartPos;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

}
