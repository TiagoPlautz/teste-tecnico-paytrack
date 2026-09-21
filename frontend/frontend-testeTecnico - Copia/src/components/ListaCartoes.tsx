import type { Cartao } from "../types/Cartao";
import { Eye } from "lucide-react";

type ListaCartoesProps = {
    listaCartoes: Cartao[];
    onDetalhes: (id: number) => void;
};

function ListaCartoes({
    listaCartoes,
    onDetalhes
}: ListaCartoesProps) {

    return (
        <div className="bg-white rounded-md shadow overflow-hidden">
            <table className="w-full table-fixed">
                <thead className="bg-slate-300">
                    <tr>
                        <th className="w-1/4 p-3 text-left">Descricao</th>
                        <th className="w-1/4 p-3 text-left">Identificador</th>
                        <th className="w-1/4 p-3 text-left">Número</th>
                        <th className="w-1/4 p-3 text-left">Status</th>
                        <th className="w-1/6 p-3 text-left">Validade</th>
                        {/* <th className="w-20 p-3 text-center">Ações</th> */}
                    </tr>
                </thead>

                <tbody>
                    {listaCartoes.map((cartao) => (
                        <tr
                            key={cartao.id}
                            className="border-t border-slate-200"
                        >
                            <td className="p-3 text-left">
                                {cartao.descricao}
                            </td>

                            <td className="p-3 text-left">
                                {cartao.identificador}
                            </td>

                            <td className="p-3 text-left">
                                {cartao.numeroCartao}
                            </td>

                            <td className="p-3 text-left">
                                {cartao.ativo ? "Ativo" : "Inativo"}
                            </td>

                            <td className="p-3 text-left">
                                {cartao.dataValidade}
                            </td>

                            {/* <td className="p-3 text-center">
                                <button
                                    onClick={() => onDetalhes(cartao.id)}
                                    className="cursor-pointer p-2 rounded hover:bg-slate-200"
                                    title="Ver detalhes"
                                >
                                    <Eye size={20} />
                                </button>
                            </td> */}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default ListaCartoes;