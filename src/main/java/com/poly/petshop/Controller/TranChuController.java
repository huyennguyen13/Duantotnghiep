@Controller
public class TranChuController {

    @Autowired
    SanPhamDao sanphamDAO;

    @GetMapping("/")
    public String home() {
        return "redirect:/customer/TrangChu";
    }

    @GetMapping("/TrangChu")
    public String redirectTrangChu() {
        return "redirect:/customer/TrangChu";
    }

    @GetMapping("/customer/TrangChu")
    public String Trangchu(@RequestParam(required = false) String error, Model model) {

        if (error != null) {
            model.addAttribute("error","bạn không có quyền truy cập");
        }

        List<SanPhamEntity> randomProducts = sanphamDAO.findRandomLowStockProducts();

        List<SanPhamEntity> limitedRandomProducts = randomProducts.stream()
                .limit(8)
                .collect(Collectors.toList());

        model.addAttribute("lowStockProducts", limitedRandomProducts);

        return "views/TrangChu";
    }

    @GetMapping("/employee/TrangQuanTri")
    public String Trangquantri(@RequestParam(required = false) String error, Model model) {

        if (error != null) {
            model.addAttribute("error", "Bạn không có quyền truy cập");
        }

        return "views/TrangQuanTri";
    }
}
