import type { DetalhesCartao } from "../types/Cartao";

type Props = {
    cartao: DetalhesCartao;
    onClose: () => void;
};

function ModalDetalhesCartao({
    cartao,
    onClose
}: Props) {

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center">

            <div className="bg-white p-6 rounded w-[500px]">

                <h2 className="text-2xl font-bold text-center mb-6">
                    Detalhes do cartão
                </h2>

                <div>
                    <p>
                        Descrição: {cartao.descricao}
                    </p>

                    <p>
                        Identificador: {cartao.identificador}
                    </p>

                    <p>
                        Número do Cartão: {cartao.numeroCartao}
                    </p>

                    <p>
                        Validade: {cartao.dataValidade}
                    </p>

                    <p>
                        Bandeira: {cartao.bandeira}
                    </p>

                    <p>
                        CVV: {cartao.cvv}
                    </p>

                    <p>
                        Status: {cartao.ativo ? "Ativo" : "Inativo"}
                    </p>

                    <p>
                        Criado em: {cartao.createdAt}
                    </p>
                </div>

                <div className="flex justify-center mt-6">
                    <button
                        onClick={onClose}
                        className="cursor-pointer mt-4 text-center bg-slate-500 text-white px-4 py-2 rounded"
                    >
                        Fechar
                    </button>
                </div>

            </div>
        </div>
    );
}

export default ModalDetalhesCartao;