import z from 'zod'
export const registerAssociateSchema = z.object({
    cpf : z.string()
    .regex(/^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/, "CPF fora do formato esperado")
    .transform((val) => val.replace(/[.-]/g, "")),
})

export type RegisterAssociateSchema = z.infer<typeof registerAssociateSchema>