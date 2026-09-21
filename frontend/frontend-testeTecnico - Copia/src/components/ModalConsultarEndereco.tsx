import { useState } from "react";
import toast from "react-hot-toast";

import { consultaEndereco } from "../services/enderecoService";
import type { ObjetoCep } from "../types/Cep";

type Props = {
    onClose: () => void;
};

function ModalConsultarEndereco({
    onClose
}: Props) {

    const [cep, setCep] = useState("");
    const [endereco, setEndereco] = useState<ObjetoCep | null>(null);
    const [consultando, setConsultando] = useState(false);

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>
    ) {
        event.preventDefault();

        try {
            setConsultando(true);

            const response = await consultaEndereco(cep);

            setEndereco(response);

            toast.success("CEP consultado com sucesso!");

        } catch (error) {

            if (error instanceof Error) {
                toast.error(error.message);
            } else {
                toast.error("Erro ao consultar CEP");
            }

        } finally {
            setConsultando(false);
        }
    }

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center">

            <div className="bg-white p-6 rounded-md w-[500px]">

                <h2 className="text-2xl font-bold text-center mb-6">
                    Consultar Endereço
                </h2>

                <form
                    onSubmit={handleSubmit}
                    className="space-y-4"
                >

                    <div className="flex flex-col gap-1">
                        <label
                            htmlFor="cep"
                            className="text-sm font-medium"
                        >
                            CEP
                        </label>

                        <input
                            id="cep"
                            type="text"
                            placeholder="00000-000"
                            value={cep}
                            onChange={(e) => setCep(e.target.value)}
                            className="w-full border p-2 rounded"
                        />
                    </div>

                    <div className="flex justify-center gap-4 mt-6">

                        <button
                            type="button"
                            onClick={onClose}
                            className="bg-slate-400 text-white px-4 py-2 rounded cursor-pointer"
                        >
                            Fechar
                        </button>

                        <button
                            type="submit"
                            disabled={consultando}
                            className="bg-blue-600 text-white px-4 py-2 rounded cursor-pointer disabled:opacity-50"
                        >
                            {consultando ? "Consultando..." : "Consultar"}
                        </button>

                    </div>

                </form>

                {endereco && (
                    <div className="mt-6 border-t pt-4">

                        <p>
                            <strong>CEP:</strong> {endereco.cep}
                        </p>

                        {/* demais campos retornados pelo backend */}

                    </div>
                )}

            </div>

        </div>
    );
}

export default ModalConsultarEndereco;