using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace SudokuHelperWeb.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            ViewBag.Title = "Sudoku Helper - Android App and Solving Techniques";
            ViewBag.HeaderTitle = "";
            return View();
        }

        public ActionResult Rules()
        {
            ViewBag.Title = "Sudoku Helper - Rules of Sudoku";
            ViewBag.HeaderTitle = "Rules of Sudoku";
            return View();
        }

        public ActionResult AppControls()
        {
            ViewBag.Title = "Sudoku Helper - How to Use the App";
            ViewBag.HeaderTitle = "How to Use the App";
            return View();
        }

        public ActionResult Techniques()
        {
            ViewBag.Title = "Sudoku Helper - Solving Techniques";
            ViewBag.HeaderTitle = "Solving Techniques";
            return View();
        }

        public ActionResult GetIt()
        {
            ViewBag.Title = "Sudoku Helper - Get It";
            ViewBag.HeaderTitle = "Get It!";
            return View();
        }

        public ActionResult History()
        {
            ViewBag.Title = "Sudoku Helper - History";
            ViewBag.HeaderTitle = "History";
            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Sudoku Helper - Contact";
            ViewBag.Title = "Sudoku Helper - Contact";
            return View();
        }
    }
}
