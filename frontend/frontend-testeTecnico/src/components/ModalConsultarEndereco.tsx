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
            setEndereco(null);
            
            const cepSemMascara = cep.replace(/\D/g, "");
            const response = await consultaEndereco(cepSemMascara);

            setEndereco(response);

            toast.success("CEP consultado com sucesso!");

        } catch (error) {

            setEndereco(null);

            if (error instanceof Error) {
                toast.error(error.message);
            } else {
                toast.error("Erro ao consultar CEP");
            }

        } finally {
            setConsultando(false);
        }
    }

    function formatarCep(valor: string) {
        const somenteNumeros = valor.replace(/\D/g, "").slice(0, 8);

        if (somenteNumeros.length <= 5) {
            return somenteNumeros;
        }

        return `${somenteNumeros.slice(0, 5)}-${somenteNumeros.slice(5)}`;
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
                            inputMode="numeric"
                            maxLength={9}
                            placeholder="00000-000"
                            value={cep}
                            onChange={(e) => {
                                setCep(formatarCep(e.target.value));
                            }}
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
                    <div className="mt-6 bg-slate-100 p-4 rounded-md">

                        <h3 className="font-bold mb-3">
                            Dados do endereço
                        </h3>

                        <div className="space-y-2">

                            <p>
                                <span className="font-semibold">CEP:</span>{" "}
                                {endereco.cep}
                            </p>

                            <p>
                                <span className="font-semibold">Logradouro:</span>{" "}
                                {endereco.logradouro}
                            </p>

                            <p>
                                <span className="font-semibold">Bairro:</span>{" "}
                                {endereco.bairro}
                            </p>

                            <p>
                                <span className="font-semibold">Cidade:</span>{" "}
                                {endereco.cidade}
                            </p>

                            <p>
                                <span className="font-semibold">UF:</span>{" "}
                                {endereco.uf}
                            </p>

                        </div>

                    </div>
                )}

            </div>

        </div>
    );
}

export default ModalConsultarEndereco;