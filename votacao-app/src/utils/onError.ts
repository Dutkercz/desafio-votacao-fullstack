import type { BackendError } from "@/types/backendError";
import type { AxiosError } from "axios";
import { toast } from "sonner";

export const onError = (error : AxiosError<BackendError>) => {
    const mensagem = error.response?.data?.detail || 'Erro desconhecido';
    toast.error("Erro: " + mensagem);
}
