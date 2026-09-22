import {
  Pagination, PaginationContent,
  PaginationItem, PaginationPrevious, PaginationLink,
  PaginationNext
} from "../ui/pagination"

type PaginationCardProps = {
  totalPages: number
  currentPage: number
  onPageChange: (page: number) => void
}

const PaginationCard = ({ totalPages, currentPage, onPageChange, }: PaginationCardProps) => {
  const pageCount = Math.max(0, Math.floor(totalPages))
  const pages = Array.from({ length: pageCount }, (_, index) => index)

  const changePage = (page: number) => {
    if (page >= 0 && page < pageCount) {
      onPageChange(page)
    }
  }

  if (pageCount <= 1) {
    return null
  }

  return (
    <Pagination>
      <PaginationContent>
        <PaginationItem>
          <PaginationPrevious
            href="#"
            aria-disabled={currentPage === 0}
            onClick={(e) => {
              e.preventDefault();
              if (currentPage > 0) {
                changePage(currentPage - 1);
              }
            }}
          />
        </PaginationItem>

        {/* pagina atual */}
        {pages.map((pageNumber) => (
          <PaginationItem key={pageNumber}>
            <PaginationLink
              href="#"
              isActive={pageNumber === currentPage}
              onClick={(e) => {
                e.preventDefault()
                changePage(pageNumber)
              }}
            >
              {pageNumber+1}
            </PaginationLink>
          </PaginationItem>
        ))}

        {/* botao avanca pagina */}
        <PaginationItem>
          <PaginationNext
            href={currentPage < pageCount ? "#" : undefined}
            aria-disabled={currentPage === pageCount}
            onClick={(e) => {
              e.preventDefault()
              changePage(currentPage + 1)
            }}
          />
        </PaginationItem>
      </PaginationContent>
    </Pagination >
  )
}

export default PaginationCard
