import { associateService } from "@/services/associateService"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { registerAssociateSchema } from "../schemas/registerAssociateSchema"
import { useMutation } from '@tanstack/react-query'
import { toast } from "sonner"
import type { AxiosError } from "axios"
import type { RegisterAssociateProps } from "../components/register/RegisterAssociateDialog"
import type { BackendError } from "@/types/backendError"
import { onError } from "@/utils/onError"
import type { AssociateRequest } from "@/types/associate"

export const useRegisterAssociateForm = ({ setOpen }: RegisterAssociateProps) => {

    const form = useForm<AssociateRequest>({
        resolver: zodResolver(registerAssociateSchema),
        defaultValues: {
            cpf: ""
        },
        shouldUnregister: true
    })

    const registerAssociateMutation = useMutation({
        mutationFn: (data: AssociateRequest) => associateService.registerAssociate(data),
        onSuccess: () => {
            setOpen(false)
            toast("Cadastro realizado com sucesso")
        },
        onError: (error: AxiosError<BackendError>) => {
            onError(error)
        }
    })

    const onSubmit = (data: AssociateRequest) => {
        registerAssociateMutation.mutate(data)
    }


    return { form, registerAssociateMutation, onSubmit }
}