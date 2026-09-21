import { useEffect, useState } from "react";
import ListaCartoes from "../components/ListaCartoes";
import ModalAdicionarCartao from "../components/ModalAdicionarCartao";
import ModalConsultarEndereco from "../components/ModalConsultarEndereco";
import { listarCartoes } from "../services/cartaoService";
import consultaEndereco from "../services/enderecoService"
import type { Cartao } from "../types/Cartao";
import type { ObjetoCep } from "../types/Cep";

function Cartoes() {
  const [listaCartoes, setListaCartoes] = useState<Cartao[]>([]);
  const [modalCadastroAberto, setModalCadastroAberto] = useState(false);
  const [modalConsultaEnderecoAberto, setModalConsultaEnderecoAberto] = useState(false);
  const [enderecoConsultado, setEnderecoConsultado] = useState<ObjetoCep | null>(null);

  useEffect(() => {
    buscarCartoes();
  }, []);

  async function buscarCartoes() {
    try {
      const response = await listarCartoes();

      setListaCartoes(response);
    } catch (error) {
      console.error("Erro ao listar cartões", error);
    }
  }

  async function consultaCep(cep: string) {
    try {
      const response = await consultaEndereco(cep);

      setEnderecoConsultado(response);
    } catch (error) {
      console.error("Erro ao consultar CEP", error);
    }
  }

  return (
    <div className="p-6">

      <div className="flex justify-between items-center mb-6">

        <h1 className="text-3xl font-bold">
          Cartões
        </h1>

        <div className="flex gap-3">

          <button
            onClick={() => setModalCadastroAberto(true)}
            className="bg-blue-600 text-white px-4 py-2 rounded cursor-pointer"
          >
            Cadastrar cartão
          </button>

          <button
            onClick={() => setModalConsultaEnderecoAberto(true)}
            className="bg-blue-600 text-white px-4 py-2 rounded cursor-pointer"
          >
            Consultar Endereço
          </button>

        </div>

      </div>

      <ListaCartoes
        listaCartoes={listaCartoes}
      />

      {modalCadastroAberto && (
        <ModalAdicionarCartao
          onClose={() => setModalCadastroAberto(false)}
          onCartaoAdicionado={buscarCartoes}
        />
      )}

      {modalConsultaEnderecoAberto && (
        <ModalConsultarEndereco
          onClose={() => setModalConsultaEnderecoAberto(false)}
          onEnderecoConsultado={consultaCep}
          endereco={enderecoConsultado}
        />
      )}

    </div>
  );
}

export default Cartoes;