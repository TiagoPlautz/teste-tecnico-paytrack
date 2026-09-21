import { useState } from "react";
import toast from "react-hot-toast";
import valid from "card-validator";
import { PaymentIcon } from "react-svg-credit-card-payment-icons";

import { cadastrarCartao } from "../services/cartaoService";

type Props = {
    onClose: () => void;
    onCartaoAdicionado: () => void;
};

function formatarNumeroCartao(valor: string) {
    const somenteNumeros = valor.replace(/\D/g, "");

    const limitado = somenteNumeros.slice(0, 16);

    return limitado.replace(/(\d{4})(?=\d)/g, "$1 ");
}

function ModalAdicionarCartao({
    onClose,
    onCartaoAdicionado
}: Props) {

    const [descricao, setDescricao] = useState("");
    const [identificador, setIdentificador] = useState("");
    const [dataValidade, setDataValidade] = useState("");
    const [numeroCartao, setNumeroCartao] = useState("");
    const [bandeira, setBandeira] = useState("");
    const [cvv, setCvv] = useState("");
    const [tipoBandeira, setTipoBandeira] = useState<string>("Generic");

    const [erro, setErro] = useState<string | null>(null);
    const [salvando, setSalvando] = useState(false);

    function formatarDataParaOffsetDateTime(data: string) {
        return `${data}T00:00:00-03:00`;
    }

    function handleNumeroCartaoChange(
        event: React.ChangeEvent<HTMLInputElement>
    ) {
        const somenteNumeros = event.target.value.replace(/\D/g, "");

        const validacao = valid.number(somenteNumeros);

        setNumeroCartao(
            formatarNumeroCartao(somenteNumeros)
        );

        if (validacao.card) {
            setBandeira(validacao.card.niceType);

            switch (validacao.card.type) {
                case "visa":
                    setTipoBandeira("Visa");
                    break;

                case "mastercard":
                    setTipoBandeira("Mastercard");
                    break;

                case "american-express":
                    setTipoBandeira("AmericanExpress");
                    break;

                case "discover":
                    setTipoBandeira("Discover");
                    break;

                case "elo":
                    setTipoBandeira("Elo");
                    break;

                case "hipercard":
                    setTipoBandeira("Hipercard");
                    break;

                default:
                    setTipoBandeira("Generic");
            }

        } else {
            setBandeira("");
            setTipoBandeira("Generic");
        }
    }

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>
    ) {
        event.preventDefault();

        const numeroSemMascara = numeroCartao.replace(/\D/g, "");

        const validacaoNumero = valid.number(numeroSemMascara);

        if (!validacaoNumero.isValid) {
            toast.error("Número do cartão inválido");
            return;
        }

        const tamanhoCvv = validacaoNumero.card?.code.size ?? 3;

        const validacaoCvv = valid.cvv(cvv, tamanhoCvv);

        if (!validacaoCvv.isValid) {
            toast.error("CVV inválido");
            return;
        }

        try {
            setSalvando(true);

            await cadastrarCartao({
                descricao,
                identificador,
                dataValidade: formatarDataParaOffsetDateTime(dataValidade),
                numeroCartao: numeroSemMascara,
                bandeira: validacaoNumero.card?.niceType ?? bandeira,
                cvv
            });

            toast.success("Cartão cadastrado com sucesso!");

            await onCartaoAdicionado();

            onClose();

        } catch (error) {

            if (error instanceof Error) {
                toast.error(error.message);
            } else {
                toast.error("Erro ao cadastrar cartão");
            }

        } finally {
            setSalvando(false);
        }
    }

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center">

            <div className="bg-white p-6 rounded-md w-[500px]">

                <h2 className="text-2xl font-bold text-center mb-6">
                    Cadastrar cartão
                </h2>

                <form
                    onSubmit={handleSubmit}
                    className="space-y-4"
                >

                    <input
                        type="text"
                        placeholder="Descrição"
                        value={descricao}
                        onChange={(e) => setDescricao(e.target.value)}
                        className="w-full border p-2 rounded"
                    />

                    <input
                        type="text"
                        placeholder="Identificador"
                        value={identificador}
                        onChange={(e) => setIdentificador(e.target.value)}
                        className="w-full border p-2 rounded"
                    />

                    <div className="relative">
                        <input
                            type="text"
                            inputMode="numeric"
                            value={numeroCartao}
                            onChange={handleNumeroCartaoChange}
                            placeholder="0000 0000 0000 0000"
                            className="w-full border p-2 pr-14 rounded"
                        />

                        {bandeira && (
                            <div className="absolute right-3 top-1/2 -translate-y-1/2">
                                <PaymentIcon
                                    type={tipoBandeira}
                                    format="flatRounded"
                                    width={36}
                                />
                            </div>
                        )}
                    </div>

                    <input
                        type="text"
                        value={bandeira}
                        readOnly
                        className="w-full border p-2 rounded bg-gray-100"
                    />

                    <input
                        type="text"
                        inputMode="numeric"
                        maxLength={3}
                        placeholder="CVV"
                        value={cvv}
                        onChange={(e) => {
                            const valor = e.target.value.replace(/\D/g, "");
                            setCvv(valor)
                        }}
                        className="w-full border p-2 rounded"
                    />

                    <div>

                        <label htmlFor="dataValidade" className="text-sm front-medium text-gray-700">
                            Data de validade do cartão:
                        </label>
                        <input
                            id="dataValidade"
                            type="date"
                            value={dataValidade}
                            onChange={(e) => setDataValidade(e.target.value)}
                            required
                            className="w-full border p-2 rounded"
                        />
                    </div>

                    {erro && (
                        <p className="text-red-500 text-sm">
                            {erro}
                        </p>
                    )}

                    <div className="flex justify-center gap-4 mt-6">

                        <button
                            type="button"
                            onClick={onClose}
                            className="bg-slate-400 text-white px-4 py-2 rounded cursor-pointer"
                        >
                            Cancelar
                        </button>

                        <button
                            type="submit"
                            disabled={salvando}
                            className="bg-blue-600 text-white px-4 py-2 rounded cursor-pointer disabled:opacity-50"
                        >
                            {salvando ? "Salvando..." : "Cadastrar"}
                        </button>

                    </div>

                </form>

            </div>

        </div>
    );
}

export default ModalAdicionarCartao;