using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CasinoPracticeWeb.Controllers
{
    public class HelpController : Controller
    {
        // GET: Help
        public ActionResult Index()
        {
            ViewBag.Title = "Casino Practice - Everyone needs a little Help sometimes";
            return View();
        }

        public ActionResult Blackjack()
        {
            ViewBag.Title = "Casino Practice - Blackjack";
            return View();
        }

        public ActionResult Crazy4Poker()
        {
            ViewBag.Title = "Casino Practice - Crazy 4 Poker";
            return View();
        }

        public ActionResult Craps()
        {
            ViewBag.Title = "Casino Practice - Craps";
            return View();
        }

        public ActionResult CaribbeanStud()
        {
            ViewBag.Title = "Casino Practice - Caribbean Stud Poker";
            return View();
        }

        public ActionResult LetItRide()
        {
            ViewBag.Title = "Casino Practice - Let It Ride";
            return View();
        }

        public ActionResult Roulette()
        {
            ViewBag.Title = "Casino Practice - Roulette";
            return View();
        }

        public ActionResult ThreeCardPoker()
        {
            ViewBag.Title = "Casino Practice - ThreeCardPoker";
            return View();
        }

    }
}